import BlogImage from "@/models/blog-image";

export const SERVER_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080";
export const uploadImage = async (body: FormData): Promise<BlogImage> => {
    const response = await fetch(`${SERVER_URL}/files/images`, {
        method: "POST",
        body: body,
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return new BlogImage(data.url, data.fileName);
}