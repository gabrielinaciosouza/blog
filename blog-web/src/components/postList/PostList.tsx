import React from "react";
import PostCard from "../postCard/PostCard";
import Link from "next/link";
import { getPosts } from "@/services/postService";
import { Button } from "@/components/ui/button";

const PostList = async () => {
    try {
        const { posts, totalCount } = await getPosts(1, 10);
        return (
            <section className="w-full mt-12 flex flex-col items-center">
                {totalCount > 0 && (
                    <div className="mb-6 w-full flex justify-center">
                        <h2 className="text-2xl font-bold text-primary text-center">Recent Stories</h2>
                    </div>
                )}
                <div className="flex flex-row flex-wrap gap-4 justify-center items-stretch w-full">
                    {posts.slice(0, 6).map((post) => (
                        <PostCard key={post.postId} {...post} />
                    ))}
                </div>
                {totalCount > 6 && (
                    <div className="mt-8 flex flex-col items-center">
                        <div className="mb-2 text-sm text-muted-foreground text-center">
                            You are viewing 6 of {totalCount} Articles.
                        </div>
                        <Link href="/posts">
                            <Button variant="secondary" className="px-6 py-3 text-base font-semibold">Load more</Button>
                        </Link>
                    </div>
                )}
            </section>
        );
    } catch {
        return <></>;
    }
};


export default PostList;
