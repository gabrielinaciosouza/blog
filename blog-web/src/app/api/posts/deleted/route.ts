
import { getDeletedPosts } from "@/services/postService";
import { NextResponse } from "next/server";
import { validateAuthResponse } from "@/services/authService";
import { parse } from "cookie";

export const GET = async (req: Request) => {
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

        const posts = await getDeletedPosts(validatedAuthResponse);

        return NextResponse.json(posts, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}