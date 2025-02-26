import { getDeletedPosts } from "@/services/postService";
import { NextResponse } from "next/server";

export const GET = async () => {
    try {
        const posts = await getDeletedPosts();

        return NextResponse.json(posts, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
}