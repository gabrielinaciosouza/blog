import { NextRequest, NextResponse } from "next/server";
import { getPosts } from "@/services/postService";

export const GET = async (req: NextRequest) => {

    try {
        const { searchParams } = new URL(req.url);
        const page = searchParams.get("page");
        const pageSize = searchParams.get("size");

        const headers = buildRequestHeaders(req);

        const { posts, totalCount } = await getPosts(Number.parseInt(page!), Number.parseInt(pageSize!), headers);

        return NextResponse.json({ posts, totalCount }, { status: 200 });
    } catch (err) {
        console.log(err);
        return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
    }
};

const buildRequestHeaders = (request: Request): Record<string, string> => {
    const headers: Record<string, string> = {
        "Content-Type": "application/json",
    };

    const ip = request.headers.get("X-Forwarded-For") || request.headers.get("Remote-Addr");
    if (ip) {
        headers["X-Forwarded-For"] = ip;
    }

    const userAgent = request.headers.get("User-Agent");
    if (userAgent) {
        headers["User-Agent"] = userAgent;
    }

    const referer = request.headers.get("Referer");
    if (referer) {
        headers["Referer"] = referer;
    }

    const visitorId = request.headers.get("X-Visitor-ID");
    if (visitorId) {
        headers["X-Visitor-ID"] = visitorId;
    }

    return headers;
}