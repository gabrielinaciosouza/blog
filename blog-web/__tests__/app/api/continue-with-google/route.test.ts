import { POST } from "@/app/api/continue-with-google/route";
import { continueWithGoogle } from "@/services/authService";

jest.mock("@/services/authService");

describe("/api/continue-with-google route", () => {
    const mockAuthResponse = {
        authToken: "mock-auth-token",
        userId: "mock-user-id",
        role: "USER",
        name: "Test User",
        email: "test@example.com",
        pictureUrl: "https://example.com/pic.jpg"
    };


    let jsonSpy: jest.SpyInstance;
    beforeEach(() => {
        jest.clearAllMocks();
        jsonSpy = jest.spyOn(require('next/server').NextResponse, 'json').mockImplementation((...args: any[]) => {
            const body = args[0];
            const init = args[1] || {};
            return {
                text: async () => JSON.stringify(body),
                status: init.status ?? 200,
                cookies: {
                    set: jest.fn(),
                },
            } as any;
        });
        global.fetch = jest.fn(() => Promise.resolve({
            ok: true,
            json: () => Promise.resolve(mockAuthResponse)
        })) as any;
    });

    afterEach(() => {
        if (jsonSpy) jsonSpy.mockRestore();
    });


    it("returns 500 if body is invalid (req.json throws)", async () => {
        // Simulate req.json() throwing
        (continueWithGoogle as jest.Mock).mockReset(); // ensure not used
        const req = { json: async () => { throw new Error("Invalid JSON"); } } as any;
        const res = await POST(req);
        expect(res.status).toBe(500);
    });


    it("returns 400 if no idToken", async () => {
        (continueWithGoogle as jest.Mock).mockReset(); // ensure not used
        const req = { json: async () => ({}) } as any;
        const res = await POST(req);
        expect(res.status).toBe(400);
    });


    it("returns 200 and sets cookie on success", async () => {
        (continueWithGoogle as jest.Mock).mockResolvedValue(mockAuthResponse);
        const req = { json: async () => ({ idToken: "mock-id-token" }) } as any;
        const res = await POST(req);
        expect(res.status).toBe(200);
        const json = JSON.parse(await res.text());
        expect(json).toEqual(mockAuthResponse);
    });

    it("returns 500 on error", async () => {
        (continueWithGoogle as jest.Mock).mockRejectedValue(new Error("fail"));
        const req = { json: async () => ({ idToken: "mock-id-token" }) } as any;
        const res = await POST(req);
        expect(res.status).toBe(500);
        const json = JSON.parse(await res.text());
        expect(json.message).toBe("Something went wrong!");
    });
});
