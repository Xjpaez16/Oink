package com.example.oink.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.oink.data.model.MovementType
import com.example.oink.data.model.User
import com.example.oink.ui.consult_movs.Consult_movs_view
import com.example.oink.ui.enter_expenses.Enter_expense_view
import com.example.oink.ui.enter_money.Enter_money_view
import com.example.oink.ui.expense.ExpenseScreen
import com.example.oink.ui.goal.GoalScreen
import com.example.oink.ui.goaldeposit.GoalDepositScreen
import com.example.oink.ui.home.BalanceScreen
import com.example.oink.ui.income.IncomeScreen
import com.example.oink.ui.loginApp.LoginScreen
import com.example.oink.ui.registerApp.RegisterScreen
import com.example.oink.ui.report.ReportScreen
import com.example.oink.ui.select_goals.select_goals_view
import com.example.oink.ui.startAplication.SplashScreen
import com.example.oink.ui.startAplication.StartUpScreen
import com.example.oink.ui.profile.ProfileScreen
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.BalanceViewModel
import com.example.oink.viewmodel.ExpenseIncomeViewModel
import com.example.oink.viewmodel.ExpenseMovemetViewModel
import com.example.oink.viewmodel.ExpenseRecurringMovementViewModel
import com.example.oink.viewmodel.GoalViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val balanceViewModel: BalanceViewModel = viewModel()
    val expenseIncomeViewModel: ExpenseIncomeViewModel = viewModel()
    val expensemovementViewModel: ExpenseMovemetViewModel = viewModel()
    val expenseRecurringViewModel: ExpenseRecurringMovementViewModel = viewModel()

    val isLoggedIn by authViewModel.isLoggedIn

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Onboarding.route) {
            StartUpScreen(
                onRegisterClick = {
                    navController.navigate(NavRoutes.Register.route) {
                        popUpTo(NavRoutes.Onboarding.route)
                    }
                },
                onLoginClick = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Onboarding.route)
                    }
                }
            )
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(viewModel = authViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(NavRoutes.Home.route) {
            val user = authViewModel.getLoggedUser()
            if (user != null) {
                BalanceScreen(
                    viewModel = balanceViewModel,
                    userName = user.name,
                    authViewModel = authViewModel,
                    onNavigateToIncome = { navController.navigate(NavRoutes.Income.route) },
                    onNavigateToExpenses = { navController.navigate(NavRoutes.Expenses.route) },
                    navController = navController
                )
            }
        }

            composable(NavRoutes.Expenses.route) {
                ExpenseScreen(
                    type = MovementType.EXPENSE,
                    viewModel = expenseIncomeViewModel,
                    authViewModel = authViewModel,
                    onNavigateToIncome = { navController.navigate(NavRoutes.Income.route) },
                    onNavigateToEnter = { navController.navigate(NavRoutes.insert_expenses.route) },
                    navController = navController
                )
            }

            composable(NavRoutes.Income.route) {
                IncomeScreen(
                    type = MovementType.INCOME,
                    viewModel = expenseIncomeViewModel,
                    authViewModel = authViewModel,
                    onNavigateToExpense = { navController.navigate(NavRoutes.Expenses.route) },
                    onNavigateToEnter = { navController.navigate(NavRoutes.insert_income.route) },
                    navController = navController
                )
            }

            composable(NavRoutes.insert_expenses.route) {
                val user = authViewModel.getLoggedUser()
                if (user != null) {
                    Enter_expense_view(
                        movementViewModel = expensemovementViewModel,
                        recurringViewModel = expenseRecurringViewModel,
                        userId = user.id,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            composable(NavRoutes.insert_income.route) {
                val user = authViewModel.getLoggedUser()
                if (user != null) {
                    Enter_money_view(
                        movementViewModel = expensemovementViewModel,
                        recurringViewModel = expenseRecurringViewModel,
                        userId = user.id,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            composable(NavRoutes.Report.route) {
                val user = authViewModel.getLoggedUser()
                if (user != null) {
                    ReportScreen(navController = navController, userName = user.name)
                }
            }

            composable(NavRoutes.SelectGoal.route) {
                val user = authViewModel.getLoggedUser()
                if (user != null) {
                    select_goals_view(
                        navController = navController,
                        userName = user.name,
                        authViewModel = authViewModel,
                        onNavigateToadd = { navController.navigate(NavRoutes.Goal.route) }
                    )
                }
            }

            composable(NavRoutes.Goal.route) {
                val goalViewModel: GoalViewModel = viewModel()
                val user = authViewModel.getLoggedUser()
                if (user != null) {
                    GoalScreen(
                        userName = user.name,
                        viewModel = goalViewModel,
                        authViewModel = authViewModel,
                        onClose = { navController.popBackStack() },
                        navController = navController
                    )
                }
            }

            composable(
                route = NavRoutes.DepositGoal.route,
                arguments = listOf(
                    navArgument("goalId") { type = NavType.StringType },
                    navArgument("goalName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val goalId = backStackEntry.arguments?.getString("goalId") ?: ""
                val goalName = backStackEntry.arguments?.getString("goalName") ?: "Meta Desconocida"
                val user = authViewModel.getLoggedUser()

                if (user != null && goalId.isNotBlank()) {
                    GoalDepositScreen(
                        userName = user.name,
                        goalId = goalId,
                        goalName = goalName,
                        authViewModel = authViewModel,
                        onClose = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }

            composable(NavRoutes.Consult_movs.route) {
                Consult_movs_view(navController = navController)
            }

            composable(NavRoutes.Profile.route) {
                val user = authViewModel.getLoggedUser()
                if (user != null) {
                    ProfileScreen(viewModel = authViewModel, onClose = { navController.popBackStack() })
                } else {
                    navController.popBackStack()
                }
            }
        }
    }