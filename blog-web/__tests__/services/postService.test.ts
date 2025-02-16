import { createPost, getPostBySlug, getPosts, POSTS_PATH } from '@/services/postService';
import CreatePostRequest from '@/models/create-post-request';
import Post from '@/models/post';

global.fetch = jest.fn();

describe('postService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });
  describe('createPost', () => {
    it('should create a new post and return the response', async () => {
      const postRequest: CreatePostRequest = {
        title: 'Test Title',
        content: 'Test Content',
      };

      const postResponse = {
        postId: '123',
        title: 'Test Title',
        content: 'Test Content',
        creationDate: '2025-02-08T00:00:00Z',
        slug: 'test-title',
      };

      (fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(postResponse),
      });

      const result = await createPost(postRequest);

      expect(fetch).toHaveBeenCalledWith(expect.any(String), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(postRequest),
      });
      expect(result).toEqual(new Post(
        postResponse.postId,
        postResponse.title,
        postResponse.content,
        postResponse.creationDate,
        postResponse.slug
      ));
    });

    it('should throw an error if the API call fails', async () => {
      const postRequest: CreatePostRequest = {
        title: 'Test Title',
        content: 'Test Content',
      };

      const errorMessage = 'Failed to publish post';
      (fetch as jest.Mock).mockResolvedValue({
        ok: false,
        json: jest.fn().mockResolvedValue({ message: errorMessage }),
      });

      await expect(createPost(postRequest)).rejects.toThrow(errorMessage);
    });
  });

  describe('getPostBySlug', () => {
    it('should fetch a post by slug successfully', async () => {
      const mockPost = {
        postId: '123',
        title: 'Test Title',
        content: 'Test Content',
        creationDate: '2025-02-08T00:00:00Z',
        slug: 'test-title',
      };

      (fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(mockPost),
      });

      const result = await getPostBySlug('test-title');

      expect(fetch).toHaveBeenCalledWith('http://127.0.0.1:8080/posts/test-title', { cache: 'force-cache' });
      expect(result).toEqual(new Post(
        mockPost.postId,
        mockPost.title,
        mockPost.content,
        mockPost.creationDate,
        mockPost.slug
      ));
    });

    it('should throw an error if the API call fails', async () => {
      const errorMessage = 'Failed to fetch post';
      (fetch as jest.Mock).mockResolvedValue({
        ok: false,
        json: jest.fn().mockResolvedValue({ message: errorMessage }),
      });

      await expect(getPostBySlug('non-existent-post')).rejects.toThrow(errorMessage);
    });
  });

  describe("getPosts", () => {
    const mockPosts = [
        { postId: "1", title: "Post 1", content: "<p>Content 1</p>", creationDate: "2025-01-01", slug: "post-1" },
        { postId: "2", title: "Post 2", content: "<p>Content 2</p>", creationDate: "2025-01-02", slug: "post-2" },
    ];

    beforeEach(() => {
        (fetch as jest.Mock).mockClear();
    });

    it("should fetch posts and return them as Post instances", async () => {
        (fetch as jest.Mock).mockResolvedValueOnce({
            ok: true,
            json: async () => mockPosts,
        });

        const result = await getPosts(1, 10);

        expect(fetch).toHaveBeenCalledWith(`${POSTS_PATH}/find`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ page: 1, size: 10 }),
        });

        expect(result).toEqual(mockPosts.map(post => new Post(post.postId, post.title, post.content, post.creationDate, post.slug)));
    });

    it("should throw an error if the response is not ok", async () => {
        (fetch as jest.Mock).mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: "Error fetching posts" }),
        });

        await expect(getPosts(1, 10)).rejects.toThrow("Error fetching posts");
    });
});

});