import { NextRequest } from 'next/server';
import { middleware } from '../src/middleware';
import AuthResponse from '@/models/auth-response';

describe('middleware', () => {
    const createRequest = (cookieValue?: string) => {
        return {
            cookies: {
                get: () => cookieValue ? { value: cookieValue } : undefined,
            },
            nextUrl: {
                url: 'http://localhost/admin',
                origin: 'http://localhost',
                pathname: '/admin',
            },
        } as unknown as NextRequest;
    };

    function createJwt(exp: number) {
        const header = Buffer.from(JSON.stringify({ alg: 'HS256', typ: 'JWT' })).toString('base64');
        const payload = Buffer.from(JSON.stringify({ exp })).toString('base64');
        return `${header}.${payload}.signature`;
    }

    function createAuthResponse(authToken: string) {
        return new AuthResponse(authToken, "userId", "ADMIN", "name", "email", "pictureUrl");
    }

    it('redirects if no token', () => {
        const res = middleware(createRequest());
        expect(res.status).toBe(307);
        expect(res.headers.get('location')).toBe('http://localhost/');
    });

    it('redirects if token is expired', () => {
        const expiredJwt = createJwt(Math.floor(Date.now() / 1000) - 10);
        const res = middleware(createRequest(JSON.stringify(createAuthResponse(expiredJwt))));
        expect(res.status).toBe(307);
        expect(res.headers.get('location')).toBe('http://localhost/');
    });

    it('allows if token is valid', () => {
        const validJwt = createJwt(Math.floor(Date.now() / 1000) + 1000);
        const authResponse = createAuthResponse(validJwt);
        const res = middleware(createRequest(JSON.stringify(authResponse)));
        expect(res.status).toBe(200);
    });

    it('redirects if token is malformed', () => {
        const res = middleware(createRequest('not.a.jwt'));
        expect(res.status).toBe(307);
        expect(res.headers.get('location')).toBe('http://localhost/');
    });
});
