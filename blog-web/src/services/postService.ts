import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/posts";

export const createPost = async (request: CreatePostRequest): Promise<Post> => {
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

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug);
};

export const getPostBySlug = async (slug: string): Promise<Post> => {
    const response = await fetch(`http://localhost:8080/posts/${slug}`, {
        cache: "force-cache",
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Failed to fetch post");
    }

    

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug);
};
