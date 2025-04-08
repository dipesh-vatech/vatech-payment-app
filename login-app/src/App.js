import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Header from "./components/Header";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import Dashboard from "./components/Dashboard";
import TransactionHistory from "./components/TransactionHistory";
import ProtectedRoute from "./components/ProtectedRoute";


function App() {
    return (
        <Router>
            <Header />
            <div>
                <h1>VaTech Login</h1>
                <Routes>
                    <Route path="/" element={<Navigate to="/login" replace />} />
                    <Route path="/login" element={<LoginForm />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route
                                        path="/dashboard"
                                        element={
                                            <ProtectedRoute>
                                                <Dashboard />
                                            </ProtectedRoute>
                                        }
                                    />
                                    <Route
                                                            path="/transaction-history"
                                                            element={
                                                                <ProtectedRoute>
                                                                    <TransactionHistory />
                                                                </ProtectedRoute>
                                                            }
                                                        />
                </Routes>
            </div>
        </Router>
    );
}

export default App;