import { NextRequest, NextResponse } from 'next/server';
import { createPost } from '@/services/postService';
import { validateAuthResponse } from '@/services/authService';
import { parse } from 'cookie';

export async function POST(req: NextRequest) {
    try {
        const cookie = req.headers.get('cookie');

        if (!cookie) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const cookies = parse(cookie)
        if (!cookies.authResponse) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }
        const validatedAuthResponse = validateAuthResponse(cookies.authResponse);

        if (!req.body) {
            return NextResponse.json({ error: 'Request body is null' }, { status: 400 });
        }
        const body = await req.json();
        const response = await createPost(validatedAuthResponse, body);
        return NextResponse.json(response, { status: 201 });
    }
    catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }

}