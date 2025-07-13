"use client";
import React from "react";
import styles from "./login.module.css";

import Button from "@/components/button/Button";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";
import { auth, googleProvider, signInWithPopup } from "@/services/firebase";
import { useRouter } from "next/navigation";



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
    const handleGoogleLogin = async () => {
        try {
            const result = await signInWithPopup(auth, googleProvider);
            const user = result.user;
            if (!user) throw new Error("No user returned from Google sign-in");
            const idToken = await user.getIdToken();
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
            alert("Google sign-in failed");
        }
    };
    return (
        <div className={styles.bgWrap}>
            <div className={styles.centeredCard}>
                <AnimatedImage src="/logo2.png" alt="Gabriel's Blog Logo" width={56} height={56} className={styles.logo} />
                <h1 className={styles.title}>Welcome</h1>
                <p className={styles.subtitle}>Sign in or create your account to access <b>Gabriel's Blog</b></p>
                <Button onClick={handleGoogleLogin} className={styles.googleBtn} ariaLabel="Sign in or Sign up with Google">
                    <GoogleIcon />
                    Continue with Google
                </Button>
            </div>
        </div>
    );
}
