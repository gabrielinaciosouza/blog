import { continueWithGoogle, validateAuthResponse } from "@/services/authService";
import AuthResponse from "@/models/auth-response";
import jwt from "jsonwebtoken";

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

describe("authService - validateAuthResponse", () => {
    const validPayload = {
        authToken: "valid-jwt-token",
        userId: "user-id",
        role: "ADMIN",
        name: "Test User",
        email: "test@example.com",
        pictureUrl: "https://example.com/pic.jpg"
    };

    function makeJwt(exp: number) {
        return jwt.sign({ exp }, "test-secret");
    }

    it("should return parsed object for valid ADMIN and unexpired token", () => {
        const exp = Math.floor(Date.now() / 1000) + 60;
        const payload = { ...validPayload, authToken: makeJwt(exp) };
        const result = validateAuthResponse(JSON.stringify(payload));
        expect(result).toMatchObject(payload);
    });

    it("should throw error for expired token", () => {
        const exp = Math.floor(Date.now() / 1000) - 60;
        const payload = { ...validPayload, authToken: makeJwt(exp) };
        expect(() => validateAuthResponse(JSON.stringify(payload))).toThrow();
    });

    it("should throw error for non-ADMIN role", () => {
        const exp = Math.floor(Date.now() / 1000) + 60;
        const payload = { ...validPayload, authToken: makeJwt(exp), role: "USER" };
        expect(() => validateAuthResponse(JSON.stringify(payload))).toThrow();
    });

    it("should throw error for invalid JWT", () => {
        const payload = { ...validPayload, authToken: "invalid.jwt.token" };
        expect(() => validateAuthResponse(JSON.stringify(payload))).toThrow();
    });

    it("should throw error for invalid JSON", () => {
        expect(() => validateAuthResponse("not-a-json-string")).toThrow();
    });
});
