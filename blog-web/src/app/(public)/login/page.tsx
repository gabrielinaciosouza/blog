"use client";
import React from "react";
import styles from "./login.module.css";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { auth, googleProvider, signInWithPopup, signInWithEmail } from "@/services/firebase";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import Loading from "@/components/loading/Loading";
import useLoading from "@/hooks/useLoading";
import { useModal } from "@/hooks/useModal";
import { useRouter } from "next/navigation";
import Image from "next/image";



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

    const passwordError =
        typeof modalState.content === "string" &&
            modalState.content.toLowerCase().includes("password")
            ? modalState.content
            : null;

    return (
        <div className="flex items-center justify-center bg-background pt-8 pb-8">
            {isLoading && <Loading />}
            <Dialog open={modalState.isOpen} onOpenChange={closeModal}>
                <DialogContent className="max-w-sm">
                    <DialogHeader>
                        <DialogTitle>Error</DialogTitle>
                    </DialogHeader>
                    <div className="text-sm text-muted-foreground">{modalState.content}</div>
                </DialogContent>
            </Dialog>
            <div className="w-full max-w-md mx-auto p-6 rounded-xl shadow-lg bg-card flex flex-col items-center gap-4">
                <Image src="/logo2.png" alt="Gabriel's Blog Logo" width={56} height={56} className="mb-2" />
                <h1 className="text-2xl font-bold text-primary mb-1">Welcome</h1>
                <p className="text-muted-foreground text-center mb-2">Sign in to access <b>Gabriel's Blog</b></p>
                <form onSubmit={handleEmailSignIn} className="w-full flex flex-col gap-4 mb-2">
                    <div className="flex flex-col gap-2">
                        <label htmlFor="email" className="text-sm font-medium text-muted-foreground">Email</label>
                        <Input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
                            placeholder="Enter your email"
                            autoComplete="email"
                            disabled={isLoading}
                        />
                        <label htmlFor="password" className="text-sm font-medium text-muted-foreground mt-2">Password</label>
                        <Input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            autoComplete="current-password"
                            disabled={isLoading}
                        />
                        {passwordError && (
                            <div className="text-xs text-destructive mt-1">{passwordError}</div>
                        )}
                    </div>
                    <Button
                        className="w-full mt-2"
                        aria-label="Sign in or Sign up with Email"
                        disabled={isLoading}
                    >
                        {isLoading ? "Signing in..." : "Continue with Email"}
                    </Button>
                </form>
                <div className="w-full flex items-center gap-2 my-2">
                    <div className="flex-1 h-px bg-border" />
                    <span className="text-xs text-muted-foreground">or</span>
                    <div className="flex-1 h-px bg-border" />
                </div>
                <Button onClick={handleGoogleLogin} className="w-full flex items-center justify-center gap-2" aria-label="Sign in or Sign up with Google" disabled={isLoading}>
                    <GoogleIcon />
                    Continue with Google
                </Button>
            </div>
        </div>
    );
}
