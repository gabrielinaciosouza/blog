"use client";
import React from "react";
import styles from "./login.module.css";

import Button from "@/components/button/Button";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";
import AuthInput from "@/components/authInput/AuthInput";
import { auth, googleProvider, signInWithPopup, signInWithEmail } from "@/services/firebase";
import Modal from "@/components/modal/Modal";
import Loading from "@/components/loading/Loading";
import useLoading from "@/hooks/useLoading";
import { useModal } from "@/hooks/useModal";
import { useRouter } from "next/navigation";
import Divider from "@/components/divider/Divider";



const GoogleIcon = () => (
    <svg width="20" height="20" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg" className={styles.googleIcon} aria-hidden="true">
        <g clipPath="url(#clip0_17_40)">
            <path d="M47.5 24.5C47.5 22.6 47.3 20.8 47 19H24V29.5H37.4C36.7 33.1 34.1 36.1 30.7 37.9V44.1H38.2C43.1 39.7 47.5 32.9 47.5 24.5Z" fill="#4285F4" />
            <path d="M24 48C30.6 48 36.2 45.8 40.2 42.2L32.7 36C30.6 37.4 27.7 38.3 24 38.3C17.7 38.3 12.2 34.1 10.3 28.6H2.5V34.9C6.5 42.1 14.6 48 24 48Z" fill="#34A853" />
            <path d="M10.3 28.6C9.7 27.2 9.4 25.7 9.4 24C9.4 22.3 9.7 20.8 10.3 19.4V13.1H2.5C0.9 16.1 0 19.4 0 24C0 28.6 0.9 31.9 2.5 34.9L10.3 28.6Z" fill="#FBBC05" />
            <path d="M24 9.7C27.7 9.7 30.6 11 32.7 12.9L40.2 6.8C36.2 3.2 30.6 1 24 1C14.6 1 6.5 6.9 2.5 14.1L10.3 19.4C12.2 14.1 17.7 9.7 24 9.7Z" fill="#EA4335" />
        </g>
        <defs>
            <clipPath id="clip0_17_40">
                <rect width="48" height="48" fill="white" />
            </clipPath>
        </defs>
    </svg>
);

export default function LoginPage() {
    const router = useRouter();
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const { isLoading, startLoading, stopLoading } = useLoading();
    const { modalState, openModal, closeModal } = useModal();

    const handleIdTokenFlow = async (idToken: string) => {
        try {
            const response = await fetch("/api/continue-with-google", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ idToken }),
            });
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message);
            }
            router.back();
        } catch (err: any) {
            openModal("Sign in failed", closeModal);
        }
    };

    const handleGoogleLogin = async () => {
        startLoading();
        try {
            const result = await signInWithPopup(auth, googleProvider);
            const user = result.user;
            if (!user) throw new Error("No user returned from Google sign-in");
            const idToken = await user.getIdToken();
            await handleIdTokenFlow(idToken);
        } catch (err: any) {
            openModal("Google sign-in failed", closeModal);
        } finally {
            stopLoading();
        }
    };

    const handleEmailSignIn = async (e: React.FormEvent) => {
        e.preventDefault();
        startLoading();
        try {
            const result = await signInWithEmail(email, password);
            const user = result.user;
            if (!user) throw new Error("No user returned from email sign-in");
            const idToken = await user.getIdToken();
            await handleIdTokenFlow(idToken);
        } catch (err: any) {
            openModal(err?.message || "Sign in failed", closeModal);
        } finally {
            stopLoading();
        }
    };

    return (
        <div className={styles.bgWrap}>
            {isLoading && <Loading />}
            <Modal isOpen={modalState.isOpen} content={modalState.content} onClose={closeModal} />
            <div className={styles.centeredCard}>
                <AnimatedImage src="/logo2.png" alt="Gabriel's Blog Logo" width={56} height={56} className={styles.logo} />
                <h1 className={styles.title}>Welcome</h1>
                <p className={styles.subtitle}>Sign in to access <b>Gabriel's Blog</b></p>
                <form onSubmit={handleEmailSignIn} className={styles.emailForm} style={{ marginBottom: 16 }}>
                    <AuthInput
                        type="email"
                        label="Email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        placeholder="Enter your email"
                        autoComplete="email"
                        error={modalState.isOpen && typeof modalState.content === 'string' && modalState.content.toLowerCase().includes("email") ? modalState.content : undefined}
                        disabled={isLoading}
                    />
                    <AuthInput
                        type="password"
                        label="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        autoComplete="current-password"
                        error={modalState.isOpen && typeof modalState.content === 'string' && modalState.content.toLowerCase().includes("password") ? modalState.content : undefined}
                        disabled={isLoading}
                    />
                    <Button
                        className={styles.emailBtn}
                        ariaLabel="Sign in or Sign up with Email"
                        disabled={isLoading}
                        onClick={() => { }}
                    >
                        {isLoading ? "Signing in..." : "Continue with Email"}
                    </Button>
                </form>
                <Divider />
                <Button onClick={handleGoogleLogin} className={styles.googleBtn} ariaLabel="Sign in or Sign up with Google" disabled={isLoading}>
                    <GoogleIcon />
                    Continue with Google
                </Button>
            </div>
        </div>
    );
}
