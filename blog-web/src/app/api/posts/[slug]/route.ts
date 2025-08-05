import { deletePost, editPost, getPostBySlug } from "@/services/postService";
import { revalidatePath } from "next/cache";
import { NextRequest, NextResponse } from "next/server";
import { parse } from "cookie";
import { validateAuthResponse } from "@/services/authService";

export async function GET(req: NextRequest, { params }: { params: Promise<{ slug: string }> }) {
    try {
        const { slug } = await params;
        const post = await getPostBySlug(slug);
        if (!post) {
            return NextResponse.json({ error: "Post not found" }, { status: 404 });
        }
        return NextResponse.json(post);
    } catch (err) {
        return NextResponse.json({ error: err instanceof Error ? err.message : "Unknown error" }, { status: 500 });
    }
}

export async function DELETE(req: NextRequest, { params }: { params: Promise<{ slug: string }> }) {
    try {
        const { slug } = await params;
        const cookie = req.headers.get('cookie');
        if (!cookie) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const cookies = parse(cookie);
        if (!cookies.authResponse) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const validatedAuthResponse = validateAuthResponse(cookies.authResponse);

        await deletePost(validatedAuthResponse, slug);
        return NextResponse.json({ message: "Post deleted successfully" }, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}

export async function PUT(req: NextRequest, { params }: { params: Promise<{ slug: string }> }) {
    try {
        const { slug } = await params;
        const cookie = req.headers.get('cookie');
        if (!cookie) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const cookies = parse(cookie);
        if (!cookies.authResponse) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const validatedAuthResponse = validateAuthResponse(cookies.authResponse);
        const requestBody = await req.json();

        await editPost(validatedAuthResponse, slug, requestBody);

        revalidatePath(`/posts/${slug}`);
        revalidatePath(`/posts`);
        return NextResponse.json({ status: 204 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}