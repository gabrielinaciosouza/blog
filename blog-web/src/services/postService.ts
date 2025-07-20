import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";

export const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:3000";
export const SERVER_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080";
export const POSTS_PATH = `${SERVER_URL}/posts`;

export const createPost = async (request: CreatePostRequest): Promise<Post> => {
    const response = await fetch(`${POSTS_PATH}`, {
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

    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug, data.coverImage);
};

export const getPostBySlug = async (slug: string): Promise<Post> => {
    const response = await fetch(`${POSTS_PATH}/${slug}`, {
        cache: "force-cache",
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }



    return new Post(data.postId, data.title, data.content, data.creationDate, data.slug, data.coverImage);
};

export const getPosts = async (page: number, size: number): Promise<{ posts: Post[], totalCount: number }> => {
    const response = await fetch(`${POSTS_PATH}/find`, {
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

    return data;
}

export const getDeletedPosts = async (): Promise<Post[]> => {
    const response = await fetch(`${POSTS_PATH}/deleted/`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data;
}

export const deletePost = async (slug: string): Promise<void> => {
    const response = await fetch(`${POSTS_PATH}/${slug}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Failed to delete post");
    }
}