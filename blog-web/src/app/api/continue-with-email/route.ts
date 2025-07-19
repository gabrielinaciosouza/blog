import { NextRequest, NextResponse } from 'next/server';
import { continueWithEmail } from '@/services/authService';

export async function POST(req: NextRequest) {
    try {
        const { email, password } = await req.json();
        if (!email || !password) {
            return NextResponse.json({ error: 'Missing email or password' }, { status: 400 });
        }

        const data = await continueWithEmail(email, password);

        const res = NextResponse.json(data, { status: 200 });
        res.cookies.set('authResponse', JSON.stringify(data), { maxAge: 3000, httpOnly: true, sameSite: 'lax', path: '/' });
        return res;
    } catch (err: any) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}
