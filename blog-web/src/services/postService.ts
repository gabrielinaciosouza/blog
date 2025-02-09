import CreatePostRequest from "@/models/create-post-request";
import CreatePostResponse from "@/models/create-post-response";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/posts";

export const createPost = async (request: CreatePostRequest): Promise<CreatePostResponse> => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Failed to publish post");
    }

    return new CreatePostResponse(data.postId, data.title, data.content, data.creationDate, data.slug);
};
