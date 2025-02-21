import { uploadImage } from '@/services/imageService'
import type { NextApiRequest, NextApiResponse } from 'next'
import { NextRequest, NextResponse } from 'next/server';

export async function POST(req: NextRequest) {

  const { searchParams } = new URL(req.url);
  const type = searchParams.get("type");

  const formData = await req.formData();

  formData.append("bucketName", type!);
  const blogImage = await uploadImage(formData);
  return NextResponse.json(blogImage)
}