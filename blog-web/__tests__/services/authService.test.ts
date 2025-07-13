import { continueWithGoogle } from "@/services/authService";
import AuthResponse from "@/models/auth-response";

describe("authService - continueWithGoogle", () => {
    const idToken = "mock-id-token";
    const mockResponse = {
        authToken: "mock-auth-token",
        userId: "mock-user-id",
        role: "USER",
        name: "Test User",
        email: "test@example.com",
        pictureUrl: "https://example.com/pic.jpg"
    };

    beforeEach(() => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                ok: true,
                json: () => Promise.resolve(mockResponse)
            })
        ) as any;
    });

    afterEach(() => {
        jest.resetAllMocks();
    });

    it("should call backend and return AuthResponse on success", async () => {
        const result = await continueWithGoogle(idToken);
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringContaining("/auth/google"),
            expect.objectContaining({
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ idToken })
            })
        );
        expect(result).toBeInstanceOf(AuthResponse);
        expect(result).toMatchObject(mockResponse);
    });

    it("should throw error if backend returns error", async () => {
        (global.fetch as jest.Mock).mockImplementationOnce(() =>
            Promise.resolve({
                ok: false,
                json: () => Promise.resolve({ message: "Backend error" })
            })
        );
        await expect(continueWithGoogle(idToken)).rejects.toThrow("Backend error");
    });
});
