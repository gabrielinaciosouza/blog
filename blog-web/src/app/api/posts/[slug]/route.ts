import { deletePost } from "@/services/postService";
import { NextRequest, NextResponse } from "next/server";
import { parse } from "cookie";
import { validateAuthResponse } from "@/services/authService";

export const DELETE = async (req: NextRequest, { params }: { params: Promise<{ slug: string }> }) => {
    try {
        const cookie = req.headers.get('cookie');
        if (!cookie) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const cookies = parse(cookie);
        if (!cookies.authResponse) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }
        const slug = (await params).slug;

        const validatedAuthResponse = validateAuthResponse(cookies.authResponse);

        await deletePost(validatedAuthResponse, slug);
        return NextResponse.json({ message: "Post deleted successfully" }, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
};