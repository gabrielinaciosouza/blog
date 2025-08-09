"use client";

import React, { useState, useRef, useEffect, Suspense } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import MarkdownContent from "@/components/MarkdownContent";
import { useRouter, useSearchParams } from "next/navigation";
import { useModal } from "@/hooks/useModal";
import useLoading from "@/hooks/useLoading";
import CreatePostRequest from "@/models/create-post-request";
import Loading from "@/components/loading/Loading";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";


function CreatePostPageInner() {
    // ...all the logic and JSX from above, unchanged...
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [coverImage, setCoverImage] = useState<string | null>(null);
    const [isFetching, setIsFetching] = useState(false);
    const textareaRef = useRef<HTMLTextAreaElement>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const router = useRouter();
    const searchParams = useSearchParams();
    const { isLoading, startLoading, stopLoading } = useLoading();
    const { modalState, openModal, closeModal } = useModal();

    const slug = searchParams.get("slug");

    // eslint-disable-next-line react-hooks/exhaustive-deps
    useEffect(() => {
        if (!slug) return;
        setIsFetching(true);
        (async () => {
            try {
                const res = await fetch(`/api/posts/${encodeURIComponent(slug)}`);
                if (!res.ok) throw new Error("Failed to fetch post");
                const data = await res.json();
                setTitle(data.title || "");
                setContent(data.content || "");
                setCoverImage(data.coverImage || null);
            } catch {
                openModal("Failed to load post for editing", () => { });
            } finally {
                setIsFetching(false);
            }
        })();
    }, [slug]);

    const insertAtCursor = (before: string, after = "") => {
        const textarea = textareaRef.current;
        if (!textarea) return;

        const start = textarea.selectionStart;
        const end = textarea.selectionEnd;
        const text = textarea.value;
        const selected = text.slice(start, end);
        const newText =
            text.slice(0, start) + before + selected + after + text.slice(end);
        setContent(newText);

        setTimeout(() => {
            textarea.focus();
            textarea.selectionStart = textarea.selectionEnd =
                start + before.length + selected.length;
        }, 0);
    };

    const handleToolbar = (action: string) => {
        switch (action) {
            case "bold":
                insertAtCursor("**", "**");
                break;
            case "italic":
                insertAtCursor("_", "_");
                break;
            case "h1":
                insertAtCursor("# ");
                break;
            case "quote":
                insertAtCursor("> ");
                break;
            case "link": {
                const url = prompt("Enter link URL");
                if (url) insertAtCursor("[", `](${url})`);
                break;
            }
            case "image": {
                const url = prompt("Enter image URL");
                if (url) insertAtCursor("![](", `${url})`);
                break;
            }
        }
    };

    const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (!e.target.files || !e.target.files[0]) return;

        const file = e.target.files[0];
        const formData = new FormData();
        formData.append("file", file);
        formData.append("fileName", file.name);
        formData.append("fileMimeType", file.type);

        try {
            const res = await fetch("/api/images?type=blog-cover-images", {
                method: "POST",
                body: formData,
            });
            const data = await res.json();
            setCoverImage(data.url);
            openModal("Image uploaded successfully", () => { });
        } catch {
            openModal("Image upload failed", () => { });
        }
    };

    const handlePublish = async () => {
        if (!title.trim() || !content.trim()) {
            openModal("Title and content are required", () => { });
            return;
        }

        startLoading();

        try {
            const isEdit = Boolean(slug);
            const apiUrl = isEdit ? `/api/posts/${encodeURIComponent(slug!)}` : '/api/create-post';
            const method = isEdit ? 'PUT' : 'POST';
            const res = await fetch(apiUrl, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(new CreatePostRequest(title, content, coverImage)),
            });
            const response = await res.json();
            const finalSlug = isEdit ? slug : response.slug;
            if (res.ok && res.status >= 200 && res.status < 300) {
                openModal("Post saved successfully", () => {
                    setTitle("");
                    setContent("");
                    setCoverImage(null);
                    router.push(`/posts/${finalSlug}`);
                });
            } else {
                openModal(response?.error || "Server error. Please try again.", () => { });
            }
        } catch {
            openModal("An error occurred", () => { });
        } finally {
            stopLoading();
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6 space-y-6">
            {(isLoading || isFetching) && <Loading />}
            <Dialog open={modalState.isOpen} onOpenChange={closeModal}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Notification</DialogTitle>
                        <DialogDescription>
                            {typeof modalState.content === "string" ? modalState.content : modalState.content}
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button onClick={closeModal}>Close</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
            <Input
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />

            <div className="flex flex-wrap gap-2">
                <Button onClick={() => handleToolbar("h1")}>H1</Button>
                <Button onClick={() => handleToolbar("bold")}>Bold</Button>
                <Button onClick={() => handleToolbar("italic")}>Italic</Button>
                <Button onClick={() => handleToolbar("quote")}>Quote</Button>
                <Button onClick={() => handleToolbar("link")}>Link</Button>
                <Button onClick={() => handleToolbar("image")}>Image</Button>
                <Button onClick={() => fileInputRef.current?.click()}>Upload Cover</Button>
                <input
                    ref={fileInputRef}
                    type="file"
                    accept="image/*"
                    style={{ display: "none" }}
                    onChange={handleImageUpload}
                />
                <Button
                    onClick={() => {
                        const input = document.createElement("input");
                        input.type = "file";
                        input.accept = "image/*";
                        input.onchange = async (e: Event) => {
                            const target = e.target as HTMLInputElement;
                            if (!target.files || !target.files[0]) return;

                            const file = target.files[0];
                            const formData = new FormData();
                            formData.append("file", file);
                            formData.append("fileName", file.name);
                            formData.append("fileMimeType", file.type);

                            try {
                                const res = await fetch("/api/images?type=blog-content-images", {
                                    method: "POST",
                                    body: formData,
                                });
                                const data = await res.json();
                                const imageUrl = data.url;
                                insertAtCursor(`![](${imageUrl})`);
                                openModal("Content image uploaded successfully", () => { });
                            } catch {
                                openModal("Failed to upload content image.", () => { });
                            }
                        };
                        input.click();
                    }}
                >
                    Upload Content Image
                </Button>

            </div>

            <textarea
                ref={textareaRef}
                className="w-full min-h-[300px] border rounded p-2 bg-background"
                placeholder="Write your post in Markdown..."
                value={content}
                onChange={(e) => setContent(e.target.value)}
            />

            <Button
                onClick={handlePublish}
                disabled={isLoading}
                className="w-full mt-4"
            >
                Publish
            </Button>

            <div>
                <h2 className="text-lg font-semibold mb-2">Live Preview</h2>
                <div className="prose prose-neutral max-w-none">
                    <MarkdownContent content={content} />
                </div>
            </div>

        </div>
    );
}

export default function CreatePostPage() {
    return (
        <Suspense fallback={<div>Loading...</div>}>
            <CreatePostPageInner />
        </Suspense>
    );
}
