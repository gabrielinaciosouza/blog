export const SERVER_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080";

import AuthResponse from "@/models/auth-response";

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