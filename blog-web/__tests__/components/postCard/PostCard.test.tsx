import React from "react";
import { render, screen } from "@testing-library/react";
import PostCard from "@/components/postCard/PostCard";
import Post from "@/models/post";

describe("PostCard Component", () => {
    const post: Post = {
        postId: "1",
        title: "Sample Post",
        content: "<p>This is a sample post content.</p>",
        creationDate: "2025-01-01",
        slug: "sample-post",
    };

    it("should render the post title", () => {
        render(<PostCard {...post} />);
        expect(screen.getByText("Sample Post")).toBeInTheDocument();
    });

    it("should render the post creation date", () => {
        render(<PostCard {...post} />);
        expect(screen.getByText("2025-01-01")).toBeInTheDocument();
    });

    it("should render the post content", () => {
        render(<PostCard {...post} />);
        expect(screen.getByText("This is a sample post content.")).toBeInTheDocument();
    });

    it("should render the post image", () => {
        render(<PostCard {...post} />);
        expect(screen.getByRole("img")).toHaveAttribute("src", "/logo2.png");
    });
});