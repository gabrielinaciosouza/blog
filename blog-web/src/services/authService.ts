export const SERVER_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080";

import AuthResponse from "@/models/auth-response";
import { jwtDecode } from "jwt-decode";
import { NextResponse } from "next/server";

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

export const validateAuthResponse = (authResponse: string): AuthResponse | NextResponse => {
    try {
        const parsedAuthResponse = JSON.parse(authResponse) as AuthResponse;
        const jwtDecoded = jwtDecode(parsedAuthResponse.authToken);
        if (!jwtDecoded || typeof jwtDecoded !== 'object' || !('exp' in jwtDecoded)) {
            return NextResponse.json({ message: "Invalid token" }, { status: 400 });
        }
        const exp = jwtDecoded.exp as number;
        if (Date.now() / 1000 > exp) {
            return NextResponse.json({ message: "Token expired" }, { status: 401 });
        }

        if (parsedAuthResponse.role !== 'ADMIN') {
            return NextResponse.json({ message: "Unauthorized" }, { status: 403 });
        }
        return parsedAuthResponse;
    } catch {
        return NextResponse.json({ message: "Invalid auth response" }, { status: 400 });
    }
}