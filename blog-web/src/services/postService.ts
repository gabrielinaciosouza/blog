import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080/posts";

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
        throw new Error(data.message);
    }

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug);
};

export const getPostBySlug = async (slug: string): Promise<Post> => {
    const response = await fetch(`${API_URL}/${slug}`, {
        cache: "force-cache",
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug);
};

export const getPosts = async (page: number, size: number): Promise<Post[]> => {
    const response = await fetch(`${API_URL}/find`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ page, size }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data.map((post: any) => new Post(post.postId, post.title, post.content, post.creationDate, post.slug));
}
