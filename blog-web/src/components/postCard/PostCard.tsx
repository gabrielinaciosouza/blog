import React from "react";
import Post from "@/models/post";
import Image from "next/image";
import Link from "next/link";
import { Button } from "@/components/ui/button";

export function stripHtml(html: string): string {
    if (!html) return "";
    return html.replace(/<[^>]*>/g, "");
}




function formatDate(dateStr: string) {
    const date = new Date(dateStr);
    return date.toLocaleDateString(undefined, {
        year: "numeric",
        month: "short",
        day: "numeric",
    });
}


const PostCard = (post: Post) => {
    const plainText = stripHtml(post.content);
    const truncated = plainText.length > 120 ? plainText.slice(0, 120) + "..." : plainText;
    return (
        <Link href={`/posts/${post.slug}`} className="block group">
            <div className="w-full max-w-[480px] sm:w-[400px] md:w-[480px] rounded-2xl overflow-hidden shadow-lg bg-background transition-all duration-200 mx-auto hover:shadow-2xl hover:scale-105 flex flex-col">
                <div className="w-full aspect-[16/9] ">
                    <Image
                        src={post.coverImage}
                        alt="Post Image"
                        width={1280}
                        height={720}
                        className="w-full h-full object-cover rounded-2xl"
                        sizes="(max-width: 700px) 100vw, 360px"
                        priority
                    />
                </div>
                <div className="flex flex-col gap-3 p-4 md:p-8 flex-1">
                    <h2 className="text-lg md:text-2xl font-bold text-primary mb-2 line-clamp-2">{post.title}</h2>
                    <div className="text-xs text-muted-foreground mb-2">{formatDate(post.creationDate)}</div>
                    <div className="text-sm text-muted-foreground line-clamp-3 mb-4 flex-1">
                        <p>{truncated}</p>
                    </div>
                    <div className="flex justify-end">
                        <Button variant="secondary" size="sm" className="px-4 py-1">Read more</Button>
                    </div>
                </div>
            </div>
        </Link>
    );
};

export default PostCard;
