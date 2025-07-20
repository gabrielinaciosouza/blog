import React from "react";
import styles from "./posts.module.css"
import PostCard from "@/components/postCard/PostCard"
import Post from "@/models/post";
import Pagination from "@/components/pagination/Pagination";
import { API_URL } from "@/services/postService";

const POST_PER_PAGE = 9;
const getData = async (page: number): Promise<{ posts: Post[], totalCount: number }> => {
    const response = await fetch(
        `${API_URL}/api/posts?page=${page}&size=${POST_PER_PAGE}`,
        {
            cache: "no-store",
        }
    );

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data;
};

const Posts = async ({ searchParams }: { searchParams: { page: number | null } }) => {
    try {
        const page = (await searchParams).page || 1;
        const { posts, totalCount } = await getData(page);
        const hasPrev = page > 1;
        const hasNext = page * POST_PER_PAGE < totalCount;

        return (
            <section className="w-full mt-12 flex flex-col items-center">
                {totalCount > 0 && (
                    <div className="mb-6 w-full flex justify-center">
                        <h2 className="text-2xl font-bold text-primary text-center">Recent Stories</h2>
                    </div>
                )}
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 xl:grid-cols-3 gap-6 w-full max-w-6xl px-2">
                    {posts.map((post) => (
                        <PostCard key={post.postId} {...post} />
                    ))}
                </div>
                <div className="mt-8 flex flex-col items-center">
                    <Pagination page={page} hasNext={hasNext} hasPrev={hasPrev} />
                </div>
            </section>
        )
    } catch (err) {
        console.log(err);
        return <></>
    }

}

export default Posts;
