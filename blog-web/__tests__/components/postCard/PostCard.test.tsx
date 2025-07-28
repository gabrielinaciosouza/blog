import React from "react";
import { render, screen } from "@testing-library/react";
import PostCard from "@/components/postCard/PostCard";
import Post from "@/models/post";

describe("PostCard Component", () => {
    it("should truncate post content longer than 120 characters", () => {
        const longContent = `<p>${"A".repeat(130)}</p>`;
        const postWithLongContent: Post = {
            postId: "2",
            title: "Long Content Post",
            content: longContent,
            creationDate: "2025-01-02",
            slug: "long-content-post",
            coverImage: "/logo2.png",
        };
        render(<PostCard {...postWithLongContent} />);
        // The rendered content should be truncated to 120 characters + '...'
        const expectedText = "A".repeat(120) + "...";
        expect(screen.getByText(expectedText)).toBeInTheDocument();
    });
    const post: Post = {
        postId: "1",
        title: "Sample Post",
        content: "<p>This is a sample post content.</p>",
        creationDate: "2025-01-01",
        slug: "sample-post",
        coverImage: "/logo2.png",
    };

    it("should render the post title", () => {
        render(<PostCard {...post} />);
        expect(screen.getByText("Sample Post")).toBeInTheDocument();
    });

    it("should render the post image with correct src", () => {
        render(<PostCard {...post} />);
        const img = screen.getByAltText("Post Image");
        expect(img).toBeInTheDocument();
        // Next.js Image may wrap src, so check for substring
        expect(img.getAttribute("src")).toContain("logo2.png");
    });

    it("should link to the correct post page", () => {
        render(<PostCard {...post} />);
        const link = screen.getByRole("link");
        expect(link).toHaveAttribute("href", "/posts/sample-post");
    });

    it("should render empty content", () => {
        const postWithNullContent = {
            postId: "2",
            title: "Long Content Post",
            creationDate: "2025-01-02",
            content: null,
            slug: "long-content-post",
            coverImage: "/logo2.png",
        };
        // @ts-expect-error
        render(<PostCard {...postWithNullContent} />);
        expect(screen.queryByText("Long Content Post")).toBeInTheDocument();
    });
});