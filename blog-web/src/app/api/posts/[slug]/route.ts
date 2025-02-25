import { deletePost } from "@/services/postService";
import { NextRequest, NextResponse } from "next/server";

export const DELETE = async (req: NextRequest, { params }: { params: Promise<{ slug: string }>}) => {
    try {
        const slug = (await params).slug;

        await deletePost(slug);
        return NextResponse.json({ message: "Post deleted successfully" }, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
};