import { NextRequest, NextResponse } from 'next/server';
import { continueWithGoogle } from '@/services/authService';

export async function POST(req: NextRequest) {
    try {
        const { idToken } = await req.json();
        if (!idToken) {
            return NextResponse.json({ error: 'Missing idToken' }, { status: 400 });
        }

        const data = await continueWithGoogle(idToken);

        const res = NextResponse.json(data, { status: 200 });
        res.cookies.set('authResponse', JSON.stringify(data), { maxAge: 3000, httpOnly: true, sameSite: 'lax', path: '/' });
        return res;
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}
