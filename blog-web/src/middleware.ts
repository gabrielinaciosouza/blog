

import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { jwtDecode } from 'jwt-decode';

export function middleware(request: NextRequest) {
    const authToken = request.cookies.get('token');
    const origin = request.nextUrl.origin;
    if (!authToken) {
        return NextResponse.redirect(`${origin}/`);
    }
    try {
        const payload = jwtDecode(authToken.value) as { exp?: number };
        if (!payload.exp || Date.now() / 1000 > payload.exp) {
            return NextResponse.redirect(`${origin}/`);
        }
        return NextResponse.next();
    } catch {
        return NextResponse.redirect(`${origin}/`);
    }
}

export const config = {
    matcher: ['/admin', '/admin/:path*', '/create-post', '/create-post/:path*'],
};