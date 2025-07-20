import React from "react";
import { render, screen, fireEvent, waitFor, within } from "@testing-library/react";
import LoginPage from "@/app/(public)/login/page";

jest.mock("@/components/button/Button", () => (props: any) => <button {...props}>{props.children}</button>);
jest.mock("@/components/animatedImage/AnimatedImage", () => (props: any) => <img {...props} />);
jest.mock("next/navigation", () => ({ useRouter: () => ({ back: jest.fn() }) }));
jest.mock("@/services/firebase", () => ({
    auth: {},
    googleProvider: {},
    signInWithPopup: jest.fn(() => Promise.resolve({ user: { getIdToken: jest.fn(() => Promise.resolve("mock-id-token")) } })),
    signInWithEmail: jest.fn((email, password) => Promise.resolve({ user: { getIdToken: jest.fn(() => Promise.resolve("mock-id-token")) } }))
}));

describe("LoginPage", () => {

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("renders login UI", () => {
        render(<LoginPage />);
        expect(screen.getByText(/Welcome/i)).toBeInTheDocument();
        expect(screen.getByText(/Continue with Google/i)).toBeInTheDocument();
        expect(screen.getByText(/Continue with Email/i)).toBeInTheDocument();
    });

    it("calls signInWithPopup and fetches /api/continue-with-google on Google button click", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        global.fetch = jest.fn(() => Promise.resolve({ ok: true, json: () => Promise.resolve({}) })) as any;
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(signInWithPopup).toHaveBeenCalled();
            expect(global.fetch).toHaveBeenCalledWith(
                "/api/continue-with-google",
                expect.objectContaining({
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ idToken: "mock-id-token" })
                })
            );
        });
    });

    it("calls signInWithEmail and fetches /api/continue-with-google on Email button click", async () => {
        const { signInWithEmail } = require("@/services/firebase");
        global.fetch = jest.fn(() => Promise.resolve({ ok: true, json: () => Promise.resolve({}) })) as any;
        render(<LoginPage />);
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: "test@email.com" } });
        fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "password123" } });
        fireEvent.click(screen.getByRole("button", { name: /Continue with Email/i }));
        await waitFor(() => {
            expect(signInWithEmail).toHaveBeenCalledWith("test@email.com", "password123");
            expect(global.fetch).toHaveBeenCalledWith(
                "/api/continue-with-google",
                expect.objectContaining({
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ idToken: "mock-id-token" })
                })
            );
        });
    });

    it("shows error modal if signInWithPopup fails", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.reject(new Error("Google sign-in failed")));
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(screen.getByText(/Google sign-in failed/i)).toBeInTheDocument();
        });
    });

    it("shows error modal if signInWithEmail fails", async () => {
        const { signInWithEmail } = require("@/services/firebase");
        signInWithEmail.mockImplementationOnce(() => Promise.reject(new Error("Email sign-in failed")));
        render(<LoginPage />);
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: "test@email.com" } });
        fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "password123" } });
        fireEvent.click(screen.getByRole("button", { name: /Continue with Email/i }));
        await waitFor(() => {
            // Check for error message in any rendered element
            expect(screen.getAllByText(/Email sign-in failed|Sign in failed/i).length).toBeGreaterThan(0);
        });
    });

    it("shows error modal if no user is returned from Google sign-in", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.resolve({ user: null }));
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(screen.getByText(/Google sign-in failed/i)).toBeInTheDocument();
        });
    });

    it("shows error modal if no user is returned from email sign-in", async () => {
        const { signInWithEmail } = require("@/services/firebase");
        signInWithEmail.mockImplementationOnce(() => Promise.resolve({ user: null }));
        render(<LoginPage />);
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: "test@email.com" } });
        fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "password123" } });
        fireEvent.click(screen.getByRole("button", { name: /Continue with Email/i }));
        await waitFor(() => {
            expect(screen.getAllByText(/No user returned from email sign-in/i).length).toBeGreaterThan(0);
        });
    });

    it("shows error modal if /api/continue-with-google response is not ok", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.resolve({ user: { getIdToken: jest.fn(() => Promise.resolve("mock-id-token")) } }));
        global.fetch = jest.fn(() => Promise.resolve({
            ok: false,
            json: () => Promise.resolve({ message: "Failed to continue with Google" })
        })) as any;
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(screen.getByText(/Sign in failed/i)).toBeInTheDocument();
        });
    });

    it("shows error modal with custom content from modalState.content (Google)", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.reject({ message: "Custom Google error" }));
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(screen.getByText(/Google sign-in failed/i)).toBeInTheDocument();
        });
    });

    it("shows error modal with custom content from modalState.content (Email)", async () => {
        const { signInWithEmail } = require("@/services/firebase");
        signInWithEmail.mockImplementationOnce(() => Promise.reject({ message: "Custom Email error" }));
        render(<LoginPage />);
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: "test@email.com" } });
        fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "password123" } });
        fireEvent.click(screen.getByRole("button", { name: /Continue with Email/i }));
        await waitFor(() => {
            expect(screen.getAllByText(/Custom Email error/i).length).toBeGreaterThan(0);
        });
    });

    it("shows error modal with default 'Sign in failed' when error has no message", async () => {
        const { signInWithEmail } = require("@/services/firebase");
        // Simulate error object without a message property
        signInWithEmail.mockImplementationOnce(() => Promise.reject({}));
        render(<LoginPage />);
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: "test@email.com" } });
        fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "password123" } });
        fireEvent.click(screen.getByRole("button", { name: /Continue with Email/i }));
        await waitFor(() => {
            expect(screen.getAllByText(/Sign in failed/i).length).toBeGreaterThan(0);
        });
    });

    it("shows AuthInput error when modalState.content includes 'password'", async () => {
        const { signInWithEmail } = require("@/services/firebase");
        signInWithEmail.mockImplementationOnce(() => Promise.reject({ message: "Password is too weak" }));
        render(<LoginPage />);
        fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: "test@email.com" } });
        fireEvent.change(screen.getByLabelText(/Password/i), { target: { value: "123" } });
        fireEvent.click(screen.getByRole("button", { name: /Continue with Email/i }));
        await waitFor(() => {
            const passwordInput = screen.getByLabelText(/Password/i);
            const passwordGroup = passwordInput.closest(".inputGroup") || passwordInput.parentElement?.parentElement;
            expect(within(passwordGroup as HTMLElement).getByText(/Password is too weak/i)).toBeInTheDocument();
        });
    });
});
