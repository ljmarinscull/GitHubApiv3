package com.example.githubapiv3.utils.mappers

interface IMapper<I, O> {
    fun map(input: I): O
}

interface IListMapper<I, O>: IMapper<List<I>, List<O>>