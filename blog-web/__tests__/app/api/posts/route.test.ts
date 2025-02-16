import { GET } from "@/app/api/posts/route";
import { getPosts } from "@/services/postService";
import { NextResponse, NextRequest } from "next/server";

jest.mock("@/services/postService", () => ({
    getPosts: jest.fn(),
}));

jest.mock('next/server', () => ({
    ...jest.requireActual('next/server'),
    NextRequest: jest.fn().mockImplementation((url: string) => ({
        url,
        nextUrl: {
            pathname: new URL(url).pathname,
            search: new URL(url).search,
            clone: jest.fn().mockReturnValue({
                pathname: new URL(url).pathname,
                search: new URL(url).search,
            }),
        },
        headers: {
            get: jest.fn().mockImplementation(key => key === 'host' ? 'localhost' : undefined),
        },
        json: jest.fn().mockResolvedValue({
            posts: [],
            count: 0,
        }),
    })),
}));

describe("GET /api/posts", () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body) => {
            return {
                json: jest.fn().mockResolvedValue(body)
            } as any;
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it("should return posts and count when request is successful", async () => {
        const posts = Array.from({ length: 9 }, (_, i) => ({
            postId: i + 1,
            title: `Post ${i + 1}`,
            content: `Content ${i + 1}`,
        }));

        (getPosts as jest.Mock).mockResolvedValue({posts, totalCount: 9});

        const request = new NextRequest("http://localhost/api/posts?page=1&size=9");

        const response = await GET(request);
        const data = await response.json();

        expect(data).toEqual({ posts: posts, totalCount: 9 });
        expect(getPosts).toHaveBeenCalledWith(1, 9);
    });

    it("should return 500 when an error occurs", async () => {
        (getPosts as jest.Mock).mockRejectedValue(new Error("Database error"));

        const request = new NextRequest("http://localhost/api/posts?page=1&size=9");

        const response = await GET(request);
        const data = await response.json();

        expect(data).toEqual({ message: "Something went wrong!" });
        expect(getPosts).toHaveBeenCalledWith(1, 9);
    });
});
