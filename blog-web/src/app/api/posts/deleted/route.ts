
import { getDeletedPosts } from "@/services/postService";
import { NextResponse } from "next/server";
import { validateAuthResponse } from "@/services/authService";

export const GET = async (req: Request) => {
    try {
        const authResponse = req.headers.get('authResponse');

        if (!authResponse) {
            return NextResponse.json({ message: "Unauthorized" }, { status: 401 });
        }

        const validatedAuthResponse = validateAuthResponse(authResponse);

        const posts = await getDeletedPosts(validatedAuthResponse);

        return NextResponse.json(posts, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}