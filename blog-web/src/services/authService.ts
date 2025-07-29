export const SERVER_URL = process.env.BLOG_API_URL || "http://127.0.0.1:8080";

import AuthResponse from "@/models/auth-response";
import { jwtDecode } from "jwt-decode";

export const continueWithGoogle = async (idToken: string): Promise<AuthResponse> => {
    const response = await fetch(`${SERVER_URL}/auth/google`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ idToken }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return new AuthResponse(
        data.authToken,
        data.userId,
        data.role,
        data.name,
        data.email,
        data.pictureUrl
    );
};

export const validateAuthResponse = (authResponse: string): AuthResponse => {
    try {
        const parsedAuthResponse = JSON.parse(authResponse) as AuthResponse;
        const jwtDecoded = jwtDecode(parsedAuthResponse.authToken);

        const exp = jwtDecoded.exp as number;
        if (Date.now() / 1000 > exp) {
            throw new Error("Token expired");
        }

        if (parsedAuthResponse.role !== 'ADMIN') {
            throw new Error("Unauthorized");
        }
        return parsedAuthResponse;
    } catch (err) {
        console.error("Error validating auth response:", err);
        throw new Error("Unknown error");
    }
}