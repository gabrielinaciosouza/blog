import { POST } from "@/app/api/create-post/route";
import { createPost } from "@/services/postService";
import { NextResponse, NextRequest } from "next/server";

jest.mock("@/services/postService", () => ({
    createPost: jest.fn(),
}));

jest.mock('next/server', () => ({
    ...jest.requireActual('next/server'),
    NextRequest: jest.fn().mockImplementation((url: string, options: any) => ({
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
        json: jest.fn().mockResolvedValue(options.body),
        body: options.body,
    })),
}));

describe("POST /api/create-post", () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body, init = { status: 200 }) => {
            return {
                json: jest.fn().mockResolvedValue(body),
                status: init.status,
            } as any;
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it("should return 400 if request body is null", async () => {
        const request = new NextRequest("http://localhost/api/create-post", { body: null });

        const response = await POST(request);
        const data = await response.json();

        expect(data).toEqual({ error: 'Request body is null' });
        expect(response.status).toBe(400);
    });

    it("should create a post and return 201 status", async () => {
        const postData = { title: "New Post", content: "Post content" };
        (createPost as jest.Mock).mockResolvedValue(postData);

        const request = new NextRequest("http://localhost/api/create-post", { body: JSON.stringify(postData) });

        const response = await POST(request);
        const data = await response.json();

        expect(data).toEqual(postData);
        expect(response.status).toBe(201);
        expect(createPost).toHaveBeenCalledWith(JSON.stringify(postData));
    });

    it("should return 500 when an error occurs", async () => {
        const postData = { title: "New Post", content: "Post content" };
        (createPost as jest.Mock).mockRejectedValue(new Error("Database error"));

        const request = new NextRequest("http://localhost/api/create-post", { body: JSON.stringify(postData) });

        const response = await POST(request);
        const data = await response.json();

        expect(data).toEqual({ message: "Something went wrong!" });
        expect(response.status).toBe(500);
        expect(createPost).toHaveBeenCalledWith(JSON.stringify(postData));
    });
});