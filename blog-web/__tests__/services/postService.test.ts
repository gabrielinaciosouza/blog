import { createPost } from '../../src/services/postService';
import CreatePostRequest from '@/models/create-post-request';
import CreatePostResponse from '@/models/create-post-response';

global.fetch = jest.fn();

describe('postService', () => {
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
      expect(result).toEqual(new CreatePostResponse(
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
});