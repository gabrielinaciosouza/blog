import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import Posts from "@/app/(public)/posts/page";

jest.mock("@/components/postCard/PostCard", () => () => <div>PostCard</div>);
jest.mock("@/components/pagination/Pagination", () => () => <div>Pagination</div>);

global.fetch = jest.fn();

describe("Posts Component", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });
    it("should render posts and pagination when data is fetched successfully", async () => {
        const response = JSON.stringify({
            posts: [
                { postId: 1, title: "Post 1", content: "Content 1", creationDate: "2025-01-01", slug: "any-slug" },
                { postId: 2, title: "Post 2", content: "Content 2", creationDate: "2025-01-01", slug: "any-slug2" },
            ],
            count: 9,
        });
        (fetch as jest.Mock).mockResolvedValue({
            ok: true,
            json: jest.fn().mockResolvedValue(JSON.parse(response)),
        });


        render(await Posts({ searchParams: { page: 1 } }));

        expect(screen.getAllByText("PostCard").length).toBe(2);
        expect(screen.getByText("Pagination")).toBeInTheDocument();
    });

    it("should handle API error gracefully", async () => {
        (fetch as jest.Mock).mockRejectedValueOnce(new Error("API Error"));

        render(await Posts({ searchParams: { page: null } }));


        expect(screen.queryByText("PostCard")).not.toBeInTheDocument();
        expect(screen.queryByText("Pagination")).not.toBeInTheDocument();
    });

    it("should handle status code not ok gracefully", async () => {
        const response = JSON.stringify({
            posts: [],
            count: 0,
        });
        (fetch as jest.Mock).mockResolvedValue({
            ok: false,
            json: jest.fn().mockResolvedValue(JSON.parse(response)),
        });

        render(await Posts({ searchParams: { page: null } }));


        expect(screen.queryByText("PostCard")).not.toBeInTheDocument();
        expect(screen.queryByText("Pagination")).not.toBeInTheDocument();
    });

    it("should correctly determine pagination states", async () => {
        const response = JSON.stringify({
            posts: Array.from({ length: 9 }, (_, i) => ({
                postId: i + 1,
                title: `Sample ${i + 1}`,
                content: "Sample content",
            })),
            count: 9,
        });
        (fetch as jest.Mock).mockResolvedValue({
            ok: true,
            json: jest.fn().mockResolvedValue(JSON.parse(response)),
        });

        render(await Posts({ searchParams: { page: 2 } }));


        expect(screen.getByText("Pagination")).toBeInTheDocument();

    });
});
