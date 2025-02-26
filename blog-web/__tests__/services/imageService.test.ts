import { uploadImage, SERVER_URL } from "@/services/imageService";
import BlogImage from "@/models/blog-image";

global.fetch = jest.fn();

describe("uploadImage", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should upload an image and return a BlogImage instance", async () => {
        const mockResponse = {
            ok: true,
            json: jest.fn().mockResolvedValue({
                url: "http://example.com/image.jpg",
                fileName: "image.jpg"
            })
        };
        (global.fetch as jest.Mock).mockResolvedValue(mockResponse);

        const formData = new FormData();
        const result = await uploadImage(formData);

        expect(global.fetch).toHaveBeenCalledWith(`${SERVER_URL}/files/images`, {
            method: "POST",
            body: formData,
        });
        expect(result).toEqual(new BlogImage("http://example.com/image.jpg", "image.jpg"));
    });

    it("should throw an error if the response is not ok", async () => {
        const mockResponse = {
            ok: false,
            json: jest.fn().mockResolvedValue({
                message: "Upload failed"
            })
        };
        (global.fetch as jest.Mock).mockResolvedValue(mockResponse);

        const formData = new FormData();

        await expect(uploadImage(formData)).rejects.toThrow("Upload failed");
        expect(global.fetch).toHaveBeenCalledWith(`${SERVER_URL}/files/images`, {
            method: "POST",
            body: formData,
        });
    });
});