import { POST } from "@/app/api/images/route";
import { uploadImage } from "@/services/imageService";
import { NextResponse, NextRequest } from "next/server";

jest.mock("@/services/imageService", () => ({
    uploadImage: jest.fn(),
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
        formData: jest.fn().mockResolvedValue(new FormData()),
    })),
}));

describe("POST /api/images", () => {
    beforeEach(() => {
        jest.spyOn(NextResponse, 'json').mockImplementation((body, init) => {
            return {
                json: jest.fn().mockResolvedValue(body),
                status: init?.status,
            } as any;
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it("should upload an image and return the response", async () => {
        const imageResponse = { url: "http://example.com/image.jpg" };
        (uploadImage as jest.Mock).mockResolvedValue(imageResponse);

        const request = new NextRequest("http://localhost/api/images?type=blog", {});

        const response = await POST(request);
        const data = await response.json();

        expect(data).toEqual(imageResponse);
        expect(uploadImage).toHaveBeenCalled();
    });

    it("should return 500 when an error occurs", async () => {
        (uploadImage as jest.Mock).mockRejectedValue(new Error("Upload error"));

        const request = new NextRequest("http://localhost/api/images?type=blog", {});

        const response = await POST(request);
        const data = await response.json();

        expect(data).toEqual({ message: "Something went wrong!" });
        expect(response.status).toBe(500);
        expect(uploadImage).toHaveBeenCalled();
    });
});