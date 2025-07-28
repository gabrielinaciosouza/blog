import { describe, it, expect } from "@jest/globals";
import { stripHtml } from "@/components/postCard/PostCard";

describe("stripHtml", () => {
    it("returns empty string for empty input", () => {
        expect(stripHtml("")).toBe("");
        expect(stripHtml(undefined as any)).toBe("");
        expect(stripHtml(null as any)).toBe("");
    });

    it("removes HTML tags and returns plain text", () => {
        expect(stripHtml("<p>Hello <b>world</b>!</p>")).toBe("Hello world!");
        expect(stripHtml("<img src='x.jpg'/>Text")).toBe("Text");
        expect(stripHtml("<div><span>Test</span></div>")).toBe("Test");
    });

    it("returns plain text unchanged if no tags", () => {
        expect(stripHtml("Just text")).toBe("Just text");
    });
});
