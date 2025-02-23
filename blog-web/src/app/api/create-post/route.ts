import { NextRequest, NextResponse } from 'next/server';
import { createPost } from '@/services/postService';

export async function POST(req: NextRequest) {
    try {
        if (!req.body) {
            return NextResponse.json({ error: 'Request body is null' }, { status: 400 });
        }
        const body = await req.json();
        const response = await createPost(body);
        return NextResponse.json(response, { status: 201 });
    }
    catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
    
}