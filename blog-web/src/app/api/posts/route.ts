import {NextRequest, NextResponse} from "next/server";
import {getPosts} from "@/services/postService";

export const GET = async (req: NextRequest) => {
    
    try {
        const { searchParams } = new URL(req.url);
        const page = searchParams.get("page");
        const pageSize = searchParams.get("size"); 

        const posts = await getPosts(Number.parseInt(page!), Number.parseInt(pageSize!));

        const count = posts.length;
        return NextResponse.json({ posts, count }, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
};
