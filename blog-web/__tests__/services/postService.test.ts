import { createPost, deletePost, getDeletedPosts, getPostBySlug, getPosts, POSTS_PATH } from '@/services/postService';
import CreatePostRequest from '@/models/create-post-request';
import Post from '@/models/post';
import AuthResponse from '@/models/auth-response';

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
        coverImage: 'cover-image-1'
      };

      const postResponse = {
        postId: '123',
        title: 'Test Title',
        content: 'Test Content',
        creationDate: '2025-02-08T00:00:00Z',
        slug: 'test-title',
        coverImage: 'cover-image-1'
      };

      const authResponse = new AuthResponse(
        'validAuthToken',
        'userId123',
        'ADMIN',
        'John Doe',
        'email@email.com',
        'http://example.com/picture.jpg'
      );

      (fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(postResponse),
      });

      const result = await createPost(authResponse, postRequest);

      expect(fetch).toHaveBeenCalledWith(expect.any(String), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${authResponse.authToken}`,
        },
        body: JSON.stringify(postRequest),
      });
      expect(result).toEqual(new Post(
        postResponse.postId,
        postResponse.title,
        postResponse.content,
        postResponse.creationDate,
        postResponse.slug,
        postResponse.coverImage
      ));
    });

    it('should throw an error if the API call fails', async () => {
      const postRequest: CreatePostRequest = {
        title: 'Test Title',
        content: 'Test Content',
        coverImage: 'cover-image-1'
      };

      const authResponse = new AuthResponse(
        'validAuthToken',
        'userId123',
        'ADMIN',
        'John Doe',
        'email@email.com',
        'http://example.com/picture.jpg'
      );

      const errorMessage = 'Failed to publish post';
      (fetch as jest.Mock).mockResolvedValue({
        ok: false,
        json: jest.fn().mockResolvedValue({ message: errorMessage }),
      });

      await expect(createPost(authResponse, postRequest)).rejects.toThrow(errorMessage);
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
        coverImage: 'cover-image-1'
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
        mockPost.slug,
        mockPost.coverImage
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
      { postId: "1", title: "Post 1", content: "<p>Content 1</p>", creationDate: "2025-01-01", slug: "post-1", coverImage: "cover-image-1" },
      { postId: "2", title: "Post 2", content: "<p>Content 2</p>", creationDate: "2025-01-02", slug: "post-2", coverImage: "cover-image-2" },
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

      expect(result).toEqual(mockPosts.map(post => new Post(post.postId, post.title, post.content, post.creationDate, post.slug, post.coverImage)));
    });

    it("should throw an error if the response is not ok", async () => {
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: false,
        json: async () => ({ message: "Error fetching posts" }),
      });

      await expect(getPosts(1, 10)).rejects.toThrow("Error fetching posts");
    });
  });

  describe('getDeletedPosts', () => {
    it('should fetch deleted posts successfully', async () => {
      const mockDeletedPosts = [
        { postId: '1', title: 'Post 1', content: '<p>Content 1</p>', creationDate: '2025-01-01', slug: 'post-1', coverImage: 'cover-image-1' },
        { postId: '2', title: 'Post 2', content: '<p>Content 2</p>', creationDate: '2025-01-02', slug: 'post-2', coverImage: 'cover-image-2' },
      ];

      (fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: jest.fn().mockResolvedValue(mockDeletedPosts),
      });

      const authResponse = new AuthResponse(
        'validAuthToken',
        'userId123',
        'ADMIN',
        'John Doe',
        'email@email.com',
        'http://example.com/picture.jpg'
      );

      const result = await getDeletedPosts(authResponse);

      expect(fetch).toHaveBeenCalledWith(`${POSTS_PATH}/deleted/`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${authResponse.authToken}`,
        },
      });
      expect(result).toEqual(mockDeletedPosts.map(post => new Post(post.postId, post.title, post.content, post.creationDate, post.slug, post.coverImage)));
    });

    it('should throw an error if the API call fails', async () => {
      const errorMessage = 'Failed to fetch deleted posts';
      (fetch as jest.Mock).mockResolvedValue({
        ok: false,
        json: jest.fn().mockResolvedValue({ message: errorMessage }),
      });

      const authResponse = new AuthResponse(
        'validAuthToken',
        'userId123',
        'ADMIN',
        'John Doe',
        'email@email.com',
        'http://example.com/picture.jpg'
      );

      await expect(getDeletedPosts(authResponse)).rejects.toThrow(errorMessage);
    });
  });

  describe('deletePost', () => {
    it('should delete a post successfully', async () => {
      (fetch as jest.Mock).mockResolvedValue({
        ok: true,
      });

      const authResponse = new AuthResponse(
        'validAuthToken',
        'userId123',
        'ADMIN',
        'John Doe',
        'email@email.com',
        'http://example.com/picture.jpg'
      );

      await deletePost(authResponse, 'test-title');

      expect(fetch).toHaveBeenCalledWith(`${POSTS_PATH}/test-title`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${authResponse.authToken}`
        }
      });
    });

    it('should throw an error if the API call fails', async () => {
      const errorMessage = 'Failed to delete post';
      (fetch as jest.Mock).mockResolvedValue({
        ok: false,
        json: jest.fn().mockResolvedValue({ message: errorMessage }),
      });

      const authResponse = new AuthResponse(
        'validAuthToken',
        'userId123',
        'ADMIN',
        'John Doe',
        'email@email.com',
        'http://example.com/picture.jpg'
      );

      await expect(deletePost(authResponse, 'non-existent-post')).rejects.toThrow(errorMessage);
    });
  });

});