import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import LoginPage from "@/app/login/page";

jest.mock("@/components/button/Button", () => (props: any) => <button {...props}>{props.children}</button>);
jest.mock("@/components/animatedImage/AnimatedImage", () => (props: any) => <img {...props} />);
jest.mock("next/navigation", () => ({ useRouter: () => ({ back: jest.fn() }) }));
jest.mock("@/services/firebase", () => ({
    auth: {},
    googleProvider: {},
    signInWithPopup: jest.fn(() => Promise.resolve({ user: { getIdToken: jest.fn(() => Promise.resolve("mock-id-token")), } }))
}));

describe("LoginPage", () => {
    it("renders login UI", () => {
        render(<LoginPage />);
        expect(screen.getByText(/Welcome/i)).toBeInTheDocument();
        expect(screen.getByText(/Continue with Google/i)).toBeInTheDocument();
    });

    it("calls signInWithPopup and fetches /api/continue-with-google on button click", async () => {
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

    it("shows alert if no user is returned from Google sign-in", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.resolve({ user: null }));
        window.alert = jest.fn();
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(window.alert).toHaveBeenCalledWith("Google sign-in failed");
        });
    });

    it("shows alert if /api/continue-with-google response is not ok", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.resolve({ user: { getIdToken: jest.fn(() => Promise.resolve("mock-id-token")) } }));
        global.fetch = jest.fn(() => Promise.resolve({
            ok: false,
            json: () => Promise.resolve({ message: "Failed to continue with Google" })
        })) as any;
        window.alert = jest.fn();
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(window.alert).toHaveBeenCalledWith("Google sign-in failed");
        });
    });

    it("shows alert on error", async () => {
        const { signInWithPopup } = require("@/services/firebase");
        signInWithPopup.mockImplementationOnce(() => Promise.reject(new Error("Google sign-in failed")));
        window.alert = jest.fn();
        render(<LoginPage />);
        fireEvent.click(screen.getByRole("button", { name: /Continue with Google/i }));
        await waitFor(() => {
            expect(window.alert).toHaveBeenCalledWith("Google sign-in failed");
        });
    });
});
