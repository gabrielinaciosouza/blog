import React, { Suspense } from "react";
import { render, screen, act, waitFor } from "@testing-library/react";
import PostList from "@/components/postList/PostList";
import { getPosts } from "@/services/postService";
import Post from "@/models/post";

jest.mock("@/services/postService");

describe("PostList Component", () => {
    const posts: Post[] = [
        { postId: "1", title: "Post 1", content: "<p>Content 1</p>", creationDate: "2025-01-01", slug: "post-1" },
        { postId: "2", title: "Post 2", content: "<p>Content 2</p>", creationDate: "2025-01-02", slug: "post-2" },
        { postId: "3", title: "Post 3", content: "<p>Content 3</p>", creationDate: "2025-01-03", slug: "post-3" },
        { postId: "4", title: "Post 4", content: "<p>Content 4</p>", creationDate: "2025-01-04", slug: "post-4" },
        { postId: "5", title: "Post 5", content: "<p>Content 5</p>", creationDate: "2025-01-05", slug: "post-5" },
        { postId: "6", title: "Post 6", content: "<p>Content 6</p>", creationDate: "2025-01-06", slug: "post-6" },
        { postId: "7", title: "Post 7", content: "<p>Content 7</p>", creationDate: "2025-01-07", slug: "post-7" },
    ];

    beforeEach(() => {
        (getPosts as jest.Mock).mockResolvedValue(posts);
    });

    it("should render the header and 'See more' link if there are more than 6 posts", async () => {
        await act(async () => {
            render(
                <Suspense fallback={<div>Loading...</div>}>
                    <PostList />
                </Suspense>
            );
        });

        await waitFor(() => {
            expect(screen.getByText("Recent Stories")).toBeInTheDocument();
            expect(screen.getByText("See more")).toBeInTheDocument();
        });
    });

    it("should render the first 6 posts", async () => {
        await act(async () => {
            render(
                <Suspense fallback={<div>Loading...</div>}>
                    <PostList />
                </Suspense>
            );
        });

        await waitFor(() => {
            for (let i = 1; i <= 6; i++) {
                expect(screen.getByText(`Post ${i}`)).toBeInTheDocument();
            }
        });
    });

    it("should not render the header and 'See more' link if there are 6 or fewer posts", async () => {
        (getPosts as jest.Mock).mockResolvedValue(posts.slice(0, 6));
        await act(async () => {
            render(
                <Suspense fallback={<div>Loading...</div>}>
                    <PostList />
                </Suspense>
            );
        });

        await waitFor(() => {
            expect(screen.queryByText("Recent Stories")).not.toBeInTheDocument();
            expect(screen.queryByText("See more")).not.toBeInTheDocument();
        });
    });

    it("should handle errors gracefully", async () => {
        (getPosts as jest.Mock).mockRejectedValue(new Error("Failed to fetch posts"));
        await act(async () => {
            render(
                <Suspense fallback={<div>Loading...</div>}>
                    <PostList />
                </Suspense>
            );
        });

        await waitFor(() => {
            expect(screen.queryByText("Recent Stories")).not.toBeInTheDocument();
        });
    });
});