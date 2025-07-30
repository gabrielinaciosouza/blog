

import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { jwtDecode } from 'jwt-decode';
import AuthResponse from './models/auth-response';

export function middleware(request: NextRequest) {
    try {
        const authResponse = JSON.parse(request.cookies.get('authResponse')?.value || '{}') as AuthResponse;
        const authToken = authResponse.authToken;
        const origin = request.nextUrl.origin;
        if (!authToken || authResponse.role !== 'ADMIN') {
            return NextResponse.redirect(`${origin}/`);
        }

        const payload = jwtDecode(authToken) as { exp?: number };
        if (!payload.exp || Date.now() / 1000 > payload.exp) {
            return NextResponse.redirect(`${origin}/`);
        }
        return NextResponse.next();
    } catch {
        return NextResponse.redirect(`${origin}/`);
    }
}

export const config = {
    matcher: ['/admin',
        '/admin/:path*',
        '/create-post',
        '/create-post/:path*',
        '/api/posts/deleted',
        '/api/create-post'],
};