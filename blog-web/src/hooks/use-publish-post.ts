import { useState } from "react";
import { useRouter } from "next/navigation";
import { createPost } from "../services/post-service";
import CreatePostRequest from "../models/create-post-request";
import CreatePostResponse from "@/models/create-post-response";

export const usePublishPost = (title: string, content: string) => {
  const [loading, setLoading] = useState(false);
  const [responseMessage, setResponseMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [postResponse, setPostResponse] = useState<CreatePostResponse | null>(null);
  const router = useRouter();

  const handlePublish = async () => {
    if (!title.trim() || !content.trim()) {
      setResponseMessage("Title and/or content cannot be empty");
      setShowModal(true);
      return;
    }

    setLoading(true);
    setResponseMessage("");

    try {
      const response = await createPost(new CreatePostRequest(title, content));
      setResponseMessage("Post saved successfully!");
      setPostResponse(response);
      setShowModal(true);
    } catch (error) {
      console.log(error);
      setResponseMessage(error instanceof Error ? error.message : "Unknown error");
      setShowModal(true);
    } finally {
      setLoading(false);
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
    if (postResponse) {
      sessionStorage.setItem(`post-${postResponse.postId}`, JSON.stringify(postResponse));
      router.push(`/posts/${postResponse.postId}`);
    }
  };  

  return {
    loading,
    responseMessage,
    showModal,
    handlePublish,
    handleCloseModal,
  };
};
