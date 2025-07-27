import { NextRequest, NextResponse } from 'next/server';
import { createPost } from '@/services/postService';
import { validateAuthResponse } from '@/services/authService';

export async function POST(req: NextRequest) {
    try {
        const authResponse = req.headers.get('authResponse');
        if (!authResponse) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const validatedAuthResponse = validateAuthResponse(authResponse);
        if (validatedAuthResponse instanceof NextResponse) {
            return validatedAuthResponse;
        }

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