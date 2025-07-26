import { NextRequest } from 'next/server';
import { middleware } from '../src/middleware';

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

    // Helper to create a valid JWT with exp
    function createJwt(exp: number) {
        const header = Buffer.from(JSON.stringify({ alg: 'HS256', typ: 'JWT' })).toString('base64');
        const payload = Buffer.from(JSON.stringify({ exp })).toString('base64');
        return `${header}.${payload}.signature`;
    }

    it('redirects if no token', () => {
        const res = middleware(createRequest());
        expect(res.status).toBe(307);
        expect(res.headers.get('location')).toBe('http://localhost/');
    });

    it('redirects if token is expired', () => {
        const expiredJwt = createJwt(Math.floor(Date.now() / 1000) - 10);
        const res = middleware(createRequest(expiredJwt));
        expect(res.status).toBe(307);
        expect(res.headers.get('location')).toBe('http://localhost/');
    });

    it('allows if token is valid', () => {
        const validJwt = createJwt(Math.floor(Date.now() / 1000) + 1000);
        const res = middleware(createRequest(validJwt));
        expect(res.status).toBe(200); // NextResponse.next() returns status 200
    });

    it('redirects if token is malformed', () => {
        const res = middleware(createRequest('not.a.jwt'));
        expect(res.status).toBe(307);
        expect(res.headers.get('location')).toBe('http://localhost/');
    });
});
