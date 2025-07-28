import React from "react";
import Post from "@/models/post";
import Image from "next/image";
import Link from "next/link";

export function stripHtml(html: string): string {
    if (!html) return "";
    return html.replace(/<[^>]*>/g, "");
}

import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

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
            <Card className="flex flex-col overflow-hidden rounded-2xl shadow-lg bg-background transition-all duration-200 w-[320px] h-[480px] mx-auto border border-border hover:shadow-2xl hover:scale-105 p-2">
                <div className="w-full h-48 rounded-t-2xl overflow-hidden">
                    <Image
                        src={post.coverImage}
                        alt="Post Image"
                        width={320}
                        height={192}
                        className="w-full h-full object-cover rounded-t-2xl"
                        sizes="(max-width: 700px) 100vw, 320px"
                        priority
                    />
                </div>
                <CardContent className="flex flex-col gap-2 p-5 flex-1">
                    <h2 className="text-xl font-bold text-primary mb-1 line-clamp-2">{post.title}</h2>
                    <div className="text-xs text-muted-foreground mb-2">{formatDate(post.creationDate)}</div>
                    <div className="text-sm text-muted-foreground line-clamp-3 mb-4 flex-1">
                        <p>{truncated}</p>
                    </div>
                    <div className="flex justify-end">
                        <Button variant="secondary" size="sm" className="px-4 py-1">Read more</Button>
                    </div>
                </CardContent>
            </Card>
        </Link>
    );
};

export default PostCard;
